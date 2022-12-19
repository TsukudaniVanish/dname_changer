package usecase

import scala.io.StdIn

import domain.Command
import domain.Command
import domain.Command

class DnameHandler(
    presenter: Presenter,
    repo:Repository,
    parser: Parser,
    driveHandler: DriveHandler,
) {

    private def listSegments(pageMax: Int= 10, pageSize: Int = 20, isFileOnly: Boolean, isDirOnly: Boolean): Unit = {
        val (files, token) = repo.FindSegments(driveHandler, pageMax, pageSize, isFileOnly, isDirOnly);
        for(file <- files) {
            println(presenter.PrettyPrintFile(file))
        }
    }

    def Exec(): Unit = {
        val command = parser.ParseArg(StdIn.readLine("dname >").split(" ").toSeq)
        execCommand(command)
    }

    private def execCommand(command: Command): Unit =
        command match
            case Command.ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => {
                listSegments(pageNum, pageMax, isFileOnly, isDirOnly)
                Exec()
            }
            case Command.Quit() => println("Bye!")
            case Command.Error() => Exec()
        

    

}