package usecase

class DnameHandler(
    presenter: Presenter,
    repo:Repository,
    driveHandler: DriveHandler,
) {

    def ListSegments(pageMax: Int= 10, pageSize: Int = 20): Unit = {
        val (files, token) = repo.FindSegments(driveHandler, pageMax, pageSize, true, false);
        for(file <- files) {
            println(presenter.PrettyPrintFile(file))
        }
    }

    

}