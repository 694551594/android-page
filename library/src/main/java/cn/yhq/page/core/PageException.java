package cn.yhq.page.core;

public class PageException extends Exception {

  private static final long serialVersionUID = 9058249553958928581L;

  public PageException(String msg) {
    super(msg);
  }

  public PageException(Throwable t) {
    super(t);
  }

}
