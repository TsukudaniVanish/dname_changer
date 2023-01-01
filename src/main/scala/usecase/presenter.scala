package usecase

trait Presenter {
    def PrettyPrintFile(file:File): String = s"Name: ${file.getName()}    (ID:${file.getID()})"

    def Show(s: String):Unit = println(s)
}