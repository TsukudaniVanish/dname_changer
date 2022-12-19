package infrastructure

import scopt.OParser
import java.io.ObjectInputFilter.Config

import domain.Command
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
                .action((_, c) => ListSegments(18, 10, false, false))
                .text("list up contents in your drive")
                .children(
                    opt[Unit]("folder-only")
                        .abbr("d")
                        .action((_, c) => c match
                            case ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => ListSegments(pageMax, pageNum, isFileOnly, true)
                            case _ => c
                        )
                        .text("show folders"),
                    opt[Int]("page-size")
                        .abbr("s")
                        .action((x, c) => c match
                            case ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => ListSegments(x, pageNum, isFileOnly, isDirOnly)
                            case _ => c
                        )
                        .text("page size"),
                    opt[Int]("page-num")
                        .abbr("n")
                        .action((f, c) => c match
                            case Command.ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => ListSegments(pageMax, f, isFileOnly, isDirOnly)
                            case _ => c
                        )
                        .text("max page number to read"),
                    checkConfig(c => c match
                        case ListSegments(pageMax, pageNum, isFileOnly, isDirOnly) => if isFileOnly && isDirOnly then failure(" can't mix file ony and folder only") else success
                        case _ => success
                    )

                ),
            cmd("quit")
                .action((_, c) => Quit())
                .text("quit this app"),
        )
    }

    def ParseArg(args: Seq[String]): Command = {
        OParser.parse(parser1, args, Command.Quit()) match
        case Some(c) => c   
        case None => Command.Error()
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