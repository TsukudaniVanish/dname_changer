package usecase

import domain.Command

trait Parser {
    def ParseArg(args: Seq[String]): Command
}