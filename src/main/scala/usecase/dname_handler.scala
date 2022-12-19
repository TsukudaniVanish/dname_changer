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

    private def listSegments(pageMax: Int= 10, pageSize: Int = 20, isFileOnly: Boolean, isDirOnly: Boolean, name: String): Unit = {
        val (files, token) = repo.FindSegments(driveHandler, pageMax, pageSize, isFileOnly, isDirOnly, name);
        for(file <- files) {
            println(presenter.PrettyPrintFile(file))
        }
    }

    def Exec(): Unit = {
        val command = parser.ParseArg(StdIn.readLine("dname > ").split(" ").toSeq)
        execCommand(command)
    }

    private def execCommand(command: Command): Unit =
        command match
            case Command.ListSegments(pageMax, pageNum, isFileOnly, isDirOnly, name) => {
                listSegments(pageNum, pageMax, isFileOnly, isDirOnly, name)
                Exec()
            }
            case Command.Quit() => println("Bye!")
            case Command.Error() => Exec()
        

    

}