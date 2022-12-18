package infrastructure

import scopt.OParser
import java.io.ObjectInputFilter.Config

import domain.Command
class Parser(
    applicationName: String
) extends usecase.Parser {
    private final val builder = OParser.builder[Command]

    private final val parser1 = {
        import domain.Command._
        import builder._
        OParser.sequence(
            programName(applicationName),
            head(applicationName, ""),
            cmd("ls")
                .action((_, c) => ListSegments(None, None, false, false))
                .text("list up contents in your drive")
                .children(
                    opt[Unit]("folder-only")
                        .abbr("fo")
                        .action((_, c) => c match
                            case ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => ListSegments(pageMax, pageNum, isFileOnly, true)
                            case _ => c
                        )
                        .text("show folders"),
                    checkConfig(c => c match
                        case ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => if isFileOnly && isDirOnly then failure(" can't mix file ony and folder only") else success
                        case _ => failure("this is not ls")
                    )

                )
        )
    }

    def ParseArg(args: Seq[String]): Command = {
        OParser.parse(parser1, args, Command.Quit()) match
        case Some(c) => c   
        case None => Command.Quit()
    }

    private enum CommandNames {
        case None 
        case ListSegments 
    }
    private class commandBuilder(name: CommandNames = CommandNames.None, options: Seq[String] = List()) {
        private val cmdName = name
        def updateOpts(opt: String): commandBuilder = commandBuilder(cmdName, options :+ opt)
    }
}