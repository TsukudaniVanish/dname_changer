package usecase

class DnameHandler(
    presenter: Presenter,
    repo:Repository,
    driveHandler: DriveHandler,
) {

    def GetAllFiles(): Unit = {
        val (files, token) = repo.FindFiles(driveHandler, 10);
        for(file <- files) {
            println(presenter.PrettyPrintFile(file))
        }
    }

}