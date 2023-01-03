// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }

  val repoMock = RepositoryMock()

  test("flip test") {
    val parserMock = ParserMock(List("rename"))
    val presenterMock = PresenterMock()
    val handler = usecase.DnameHandler(presenterMock, repoMock, parserMock)
    handler.Exec()
    assertEquals(
      presenterMock.Shows,
      List("this is err", "Bye!")
    )
    val parserMock2 = ParserMock(List("rename", "ok", "1"))
    val presenterMock2 = PresenterMock()
    val handler2 = usecase.DnameHandler(presenterMock2, repoMock, parserMock2)
    handler2.Exec()
    assertEquals(
      presenterMock2.Shows,
      List("Name: ok    (ID:1)", "Bye!")
    )
  }

  test("cutoutCopyOf") {
    val parserMock = ParserMock(List("rename"))
    val presenterMock = PresenterMock()
    val handler = usecase.DnameHandler(presenterMock, repoMock, parserMock)
    val testItems = List("aaabs", "にほんご.py")
    for(item <- testItems) {
      val copyOf = item ++ " のコピー"
      assertEquals(
        handler.cutoutCopyOf(copyOf),
        item
      )
    }
  }
}
