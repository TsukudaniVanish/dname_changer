package infrastructure

import repository as R 
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;

import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader

import scala.jdk.CollectionConverters._
import java.nio.charset.StandardCharsets
import javax.management.Query
import scala.compiletime.ops.boolean


class DriveHandler(
    applicationName: String, 
    tokensDirectoryPath: String,
    scopes: Seq[String],
    credentialPath: String,
    jsonFactory: JsonFactory,
) extends R.DriveHandler {

    private final def httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    private final def service = Drive.Builder(httpTransport, jsonFactory, getCredentials(httpTransport)).setApplicationName(applicationName).build();

    private val folderMimeType: String = "application/vnd.google-apps.folder"

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private def getCredentials(httpTransport: NetHttpTransport): Credential = {
       val in: InputStream = getClass.getResourceAsStream(credentialPath) match
            case null => throw FileNotFoundException("Resource not found:" ++ credentialPath)
            case input => input;

        val googleClientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(in, StandardCharsets.UTF_8));

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, googleClientSecrets, scopes.asJava
        ).setDataStoreFactory(FileDataStoreFactory(java.io.File(tokensDirectoryPath)))
        .setAccessType("offline")
        .build();

        val recover = LocalServerReceiver.Builder().setPort(8888).build();

        AuthorizationCodeInstalledApp(flow, recover).authorize("user")
    }

    def GetMaxPageSize(): Int = {
        service.files().list().getPageSize()
    }

    def FindFiles(
        pageSize: Int, 
        nextPageToken: String,
        isFileOnly: Boolean,
        isFolderOnly: Boolean,
        name: String,
        parentId: Option[String],
    ): (Seq[File], String) = {
        val query: String = QueryBuilder(List())
            .IsFileOnly(isFileOnly)
            .IsFolderOnly(isFolderOnly)
            .NameContains(name)
            .IDInParents(parentId)
            .Build();
        val serviceRequest = service.files().list()
            .setPageToken(nextPageToken)
            .setPageSize(pageSize)
            .setFields("nextPageToken, files(id, name)")
        val result = if query.isBlank() then serviceRequest.execute() else serviceRequest.setQ(query).execute()
        val pageToken = result.getNextPageToken() match
            case null => ""
            case s => s 
        (result.getFiles().asScala.toList.map(f => File(f)), pageToken)
    }
    

    private class QueryBuilder(queries: Seq[String] = List()) {

        private def mimeType(mime: String): QueryBuilder = QueryBuilder( queries :+ s"mimeType ='${mime}'")

        private def mimeTypeNot(mime: String): QueryBuilder = QueryBuilder(queries :+ s"mimeType != '${mime}'")

        private def nameContains(name: String): QueryBuilder = QueryBuilder(queries :+ s"name contains '${name}'")

        private def parentsIn(id:String): QueryBuilder = QueryBuilder(queries :+ s"'${id}' in parents")

        def IsFileOnly(isFileOnly: Boolean) = if isFileOnly then mimeTypeNot(folderMimeType) else QueryBuilder(queries)
        
        def IsFolderOnly(isFolderOnly: Boolean) = if isFolderOnly then mimeType(folderMimeType) else QueryBuilder(queries) 
    
        def NameContains(name: String) = if name.isBlank() then QueryBuilder(queries) else nameContains(name)

        def IDInParents(optId: Option[String]) = optId match
            case None => QueryBuilder(queries) 
            case Some(value) => parentsIn(value)

        def Build(): String = {
            val input = queries.fold("")((s, t) => if s.isBlank then  t else s + " and " + t);
            if input.isBlank() then 
                "trashed = false"
            else 
                input + " and trashed = false"
        }
    }
} 