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
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import scala.io.Source
import scala.util.Try
import java.io.InputStream
import java.io.InputStreamReader
import java.io.FileNotFoundException
import scala.util.Success
import scala.util.Failure

import collection.convert.ImplicitConversionsToJava as JavaList
import scala.collection.JavaConverters._

object DriveQuickStart {

    private final def APPLICATION_NAME: String = "Google Drive API QuickStart"

    private final def JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private final def TOKENS_DIRECTORY_PATH: String = "tokens"

    private final def SCOPES: Seq[String] = List(DriveScopes.DRIVE_METADATA_READONLY)

    private final def CREDENTIALS_FILE_PATH: String = "client_secret.json"

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private def getCredentials(httpTransport: NetHttpTransport): Credential = {
        val in: InputStream = getClass.getResourceAsStream(CREDENTIALS_FILE_PATH) match
            case null => throw FileNotFoundException("Resource not found:" ++ CREDENTIALS_FILE_PATH)
            case input => input
        

        val googleClientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(in))

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, googleClientSecrets, JavaList.`seq AsJavaList`(SCOPES)
        ).setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();

        val recover = LocalServerReceiver.Builder().setPort(8888).build()

        AuthorizationCodeInstalledApp(flow, recover).authorize("user")
    }

    private def getResourceFromSourcePath(path:String): Try[InputStreamReader] =
        Try(Source.fromResource(path).reader())
            .recover(throw FileNotFoundException("Resource not found: " ++ path))


    def runApp():Unit = {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport)).setApplicationName(APPLICATION_NAME).build()

        val result = service.files().list()
            .setPageSize(18)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val files = result.getFiles()
        val nextToken = result.getNextPageToken()
        if files == null || files.isEmpty() then println("Files not found.")
        else 
            println("Files:")
            for(file <- files.asScala.toList) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId())
            }
    }
}

@main def run: Unit = 
    DriveQuickStart.runApp()