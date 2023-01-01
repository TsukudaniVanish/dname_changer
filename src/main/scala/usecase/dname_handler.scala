package usecase

import scala.io.StdIn

import domain.Command
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

    private def updateFiles(
        input: Seq[domain.UpdateFileInput],
    ): Seq[File] = {
        input.map(input => repo.UpdateFile(input)).flip() match
            case Left(errMsgs) => {
                println(errMsgs)
                for(errMsg <- errMsgs) {
                    presenter.Show(errMsg)
                }
                List()
            }
            case Right(files) => files
    }

    def Exec(): Unit = {
        val command = parser.ParseArg(parser.getArg())
        execCommand(command)
    }

    private def execCommand(command: Command): Unit = {
        val files = command match {
            case Command.ListSegments(input) => listSegments(input)
            case Command.RenameFile(updateFileInput) => updateFiles(List(updateFileInput))
            case Command.Quit() => return presenter.Show("Bye!")
            case Command.Error() => return Exec()
        }
        for(file <- files) {
            presenter.Show(presenter.PrettyPrintFile(file))
        }
        Exec()
    }
        
        

    /**
      * if find lef then returns lefts
      * all elements are right then returns seq[b]
    */
    extension[A,B] (seq: Seq[Either[A, B]])
        def flip(): Either[Seq[A], Seq[B]] = {
            if seq.indexWhere(e => e match
                case Left(value) => false 
                case Right(value) => true 
            ) == -1 then 
                Left(
                    seq.filter(e => e match
                        case Left(value) => true
                        case Right(value) => false
                    ).map(e => e match
                        case Left(value) => value
                        case Right(value) => ??? 
                    )
                )
            else 
                Right(
                    seq.filter(e => e match
                        case Left(value) => false
                        case Right(value) => true
                    ).map(e => e match
                        case Right(value) => value
                        case Left(value) => ??? 
                    )
                )
        }
}