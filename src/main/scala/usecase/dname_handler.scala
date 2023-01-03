package usecase


import domain.Command
import domain.Command
import domain.Command
import domain.Command
import domain.Command
import domain.CutCopyOfInput
import scala.util.matching.Regex
import scala.compiletime.ops.string
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

    private def cutCopyOf(
      input: CutCopyOfInput
    ): Seq[File] = {
      val (targetFiles, _) = repo.FindSegments(domain.LSInput(pageMax = 18, pageSize = 20, isFileOnly = true, isDirOnly = false, parentId = Some(input.parentID)));
      updateFiles(
        targetFiles
          .filter(hasCopyOfName)
          .map(
            file => domain.UpdateFileInput(
              cutoutCopyOf(file.getName()),
              file.getID()
            )
          )
        )
    }

    private def showUseage(): Seq[File] = {
      presenter.Show(parser.getUseage());
      List()
    }

    // check file name has ".* のコピー" 
    private def hasCopyOfName(file: File): Boolean = {
      val targetReg: Regex = "^(.*) のコピー$".r;
      targetReg.findFirstMatchIn(file.getName()) match
        case None => false 
        case Some(value) => true
    }

    def cutoutCopyOf(name: String): String = {
      val targetReg =  "^(.*) のコピー$".r;
      targetReg.findFirstMatchIn(name) match
        case None => name 
        case Some(value) => {
          name.slice(from = 0, until = name.length() - " のコピー".length())
        }
    }
                    

    def Exec(): Unit = {
        val command = parser.ParseArg(parser.getArg())
        execCommand(command)
    }

    private def execCommand(command: Command): Unit = {
        val files = command match {
            case Command.ListSegments(input) => listSegments(input)
            case Command.RenameFile(updateFileInput) => updateFiles(List(updateFileInput))
            case Command.CutCopyOf(cutCopyOfInput) => cutCopyOf(cutCopyOfInput)
            case Command.Help() => showUseage()
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
