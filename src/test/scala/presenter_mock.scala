class PresenterMock extends usecase.Presenter {
    var Shows: List[String] = List()

    override def Show(s: String): Unit = {
        this.Shows = this.Shows.appended(s)
        println(s)
        ()
    } 
}