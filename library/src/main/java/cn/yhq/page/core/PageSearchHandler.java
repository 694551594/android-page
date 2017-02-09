package cn.yhq.page.core;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

class PageSearchHandler<T, I> implements IPageRequester<T, I> {
    private String mKeyword;
    private T mPageData;
    private IPageDataParser<T, I> mPageDataParser;
    private IPageSearcher<T, I> mPageSearcher;

    public PageSearchHandler(IPageSearcher<T, I> pageSearcher) {
        this.mPageSearcher = pageSearcher;
    }

    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
    }

    public void setPageData(T pageData) {
        this.mPageData = pageData;
    }

    public void setPageDataParser(IPageDataParser<T, I> pageDataParser) {
        this.mPageDataParser = pageDataParser;
    }

    @Override
    public void onRequest(PageAction pageAction, Page<I> page, IPageResponse<T> pageResponse) {
        this.mPageSearcher.onSearch(mPageData, mKeyword, page, pageResponse);
    }

    @Override
    public void onCancel() {
        this.mPageSearcher.onCancel();
    }
}
