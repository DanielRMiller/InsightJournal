package InsightJournal;

class Scripture{
   private String book;
   private int chapter;
   private int startVerse;
   private int endVerse;

    Scripture(){
    book = "";
    chapter = 0;
    startVerse = 0;
    endVerse = 0;
    }
    
    Scripture(String book, int chapter, int verse) {
    }

   // GETTERS
   public String getBook(){return book;}
   public int getChapter(){return chapter;}
   public int getStartVerse(){return startVerse;}
   public int getEndVerse(){return endVerse;}

   // SETTERS
   public void setBook(String newBook) {book = newBook;}
   public void setChapter(int newChapter){chapter = newChapter;}
   public void setStartVerse(int newStartVerse){startVerse = newStartVerse;}
   public void setEndVerse(int newEndVerse){endVerse = newEndVerse;}
   public String display() {
       String rString = "Scripture Display:" + 
               "\nBook: " + book + 
               "\nChapter: " + chapter + 
               "\nStartVerse: " + startVerse +
               "\nEndVerse: " + endVerse + "\n";
       return rString;
   }
}