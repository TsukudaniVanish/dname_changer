package usecase

trait Repository {
    /**
      * 
      *
      * @return files, nextPageToken
      */
    def FindSegments(
        lsInput: domain.LSInput,
    ): (Seq[File], String)
}