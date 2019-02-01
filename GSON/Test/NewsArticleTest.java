public class NewsArticleTest {



    @Test
    public void getAuthor{
        Gson gson = new Gson();
        NewsArticle newsArticle = gson.fromJson(ARTICLE_JSON, NewsArticle.class);
    }
}
