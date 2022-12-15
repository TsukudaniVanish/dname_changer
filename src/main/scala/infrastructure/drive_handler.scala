package infrastructure

import usecase as U 
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

class DriveHandler(
    applicationName: String, 
    tokensDirectoryPath: String,
    scopes: Seq[String],
    credentialPath: String,
    jsonFactory: JsonFactory,
) extends U.DriveHandler {

    private final def httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    private final def service = Drive.Builder(httpTransport, jsonFactory, getCredentials(httpTransport)).setApplicationName(applicationName).build();


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
            case input => input
        

        val googleClientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(in, StandardCharsets.UTF_8));

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, googleClientSecrets, scopes.asJava
        ).setDataStoreFactory(FileDataStoreFactory(java.io.File(tokensDirectoryPath)))
        .setAccessType("offline")
        .build();

        val recover = LocalServerReceiver.Builder().setPort(8888).build()

        AuthorizationCodeInstalledApp(flow, recover).authorize("user")
    }

    def GetMaxPageSize(): Int = {
        service.files().list().getPageSize()
    }

    def FindFiles(pageSize: Int, nextPageToken: String): (Seq[File], Option[String]) = {
        val result = service.files().list()
            .setPageToken(nextPageToken)
            .setPageSize(pageSize)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val pageToken = result.getNextPageToken() match 
            case "" => None
            case s => Some(s)
        (result.getFiles().asScala.toList.map(f => File(f)), pageToken)
    }


} 