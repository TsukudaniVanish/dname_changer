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
    ): Unit = {
        val (files, token) = repo.FindSegments(input);
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
            case Command.ListSegments(input) => {
                listSegments(input)
                Exec()
            }
            case Command.Quit() => println("Bye!")
            case Command.Error() => Exec()
        

    

}