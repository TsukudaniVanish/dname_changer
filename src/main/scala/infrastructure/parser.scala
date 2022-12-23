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
                .action((_, c) => ListSegments(domain.LSInput()))
                .text("list up contents in your drive")
                .children(
                    opt[Unit]("folder-only")
                        .abbr("d")
                        .action((_, c) => c match
                            case ListSegments(input) => ListSegments(domain.LSInput(input.PageNum, input.PageSize, input.IsFileOnly, true, input.Name, input.OptParentId))
                            case _ => c
                        )
                        .text("show folders"),
                    opt[Int]("page-size")
                        .abbr("s")
                        .action((x, c) => c match
                            case ListSegments(input) => ListSegments(domain.LSInput(input.PageNum, x, input.IsFileOnly, input.IsDirOnly, input.Name, input.OptParentId))
                            case _ => c
                        )
                        .text("page size"),
                    opt[Int]("page-num")
                        .abbr("n")
                        .action((f, c) => c match
                            case Command.ListSegments(input) => ListSegments(domain.LSInput(f, input.PageSize, input.IsFileOnly, input.IsDirOnly, input.Name, input.OptParentId))
                            case _ => c
                        )
                        .text("max page number to read"),
                    opt[String]("name")
                        .abbr("w")
                        .action((s, c) => c match
                            case Command.ListSegments(input) => ListSegments(domain.LSInput(input.PageNum, input.PageSize, input.IsFileOnly, input.IsDirOnly, s, input.OptParentId))
                            case _ => c 
                        )
                        .text("keywords contained in name"),
                    opt[String]("parent-id")
                        .abbr("p")
                        .action((s, c) => c match
                            case Command.ListSegments(input) => ListSegments(domain.LSInput(input.PageNum, input.PageSize, input.IsFileOnly, input.IsDirOnly, input.Name, Some(s)))
                            case _ => c
                        )
                        .text("set parent id and constrain parent of segments"),
                    checkConfig(c => c match
                        case ListSegments(input) => if input.IsFileOnly && input.IsDirOnly then failure(" can't mix file ony and folder only") else success
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
}