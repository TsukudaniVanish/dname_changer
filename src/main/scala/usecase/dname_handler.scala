package usecase

import scala.io.StdIn

import domain.Command
import domain.Command
import domain.Command

class DnameHandler(
    presenter: Presenter,
    repo:Repository,
    parser: Parser,
) {

    private def listSegments(
       input: domain.LSInput,
    ): Seq[File] = {
        val (files, token) = repo.FindSegments(input);
        files
    }

    def Exec(): Unit = {
        val command = parser.ParseArg(StdIn.readLine("dname > ").split(" ").toSeq)
        execCommand(command)
    }

    private def execCommand(command: Command): Unit = {
        val files = command match {
            case Command.ListSegments(input) => listSegments(input)
            case Command.Quit() => return println("Bye!")
            case Command.Error() => return Exec()
        }
        for(file <- files) {
            println(presenter.PrettyPrintFile(file))
        }
        Exec()
    }
        
        

    

}