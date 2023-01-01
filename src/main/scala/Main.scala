import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;



object ServiceProvider {

    private final def APPLICATION_NAME: String = "Google Drive API QuickStart"

    private final def JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private final def TOKENS_DIRECTORY_PATH: String = "tokens"

    /**
      * If modifying these scopes, delete your previously saved tokens/ folder.
      */ 
    private final def SCOPES: Seq[String] = List(DriveScopes.DRIVE)

    private final def CREDENTIALS_FILE_PATH: String = "client_secret.json"

    def runApp():Unit = {
       val parser = infrastructure.Parser("dname")
       val driveHandler = infrastructure.DriveHandler(APPLICATION_NAME, TOKENS_DIRECTORY_PATH, SCOPES, CREDENTIALS_FILE_PATH, JSON_FACTORY)
       val dnameHandler = usecase.DnameHandler(
            presenter.Presenter(),
            repository.Repository(
                driveHandler,
            ),
            parser,
       )
       dnameHandler.Exec()
    }
}

@main def run(): Unit = 
    ServiceProvider.runApp()