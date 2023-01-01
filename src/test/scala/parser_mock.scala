import domain.Command
class ParserMock(args: List[String]) extends usecase.Parser {
    var Arg:Seq[String] = args
    def ParseArg(args: Seq[String]): Command = args match 
        case "rename" +: tail  =>  {
            Arg = List();
            if tail.length != 2 then 
                Command.RenameFile(domain.UpdateFileInput("", ""))
            else 
                Command.RenameFile(domain.UpdateFileInput(tail.toList(0), tail.toList(1)))
        }
        case _ => Command.Quit()
    def getArg(): Seq[String] = Arg
}