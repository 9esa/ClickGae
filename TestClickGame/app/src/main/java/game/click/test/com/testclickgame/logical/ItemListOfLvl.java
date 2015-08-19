package game.click.test.com.testclickgame.logical;

/**
 * Created by kirichenko on 18.08.2015.
 */
public class ItemListOfLvl {

    private int iLvl, iCountToUp, iClickPrice;

    public ItemListOfLvl(int iLvl, int iCountToUp, int iClickPrice) {
        this.iLvl = iLvl;
        this.iCountToUp = iCountToUp;
        this.iClickPrice = iClickPrice;
    }

    public int getiLvl() {
        return iLvl;
    }

    public void setiLvl(int iLvl) {
        this.iLvl = iLvl;
    }

    public int getiCountToUp() {
        return iCountToUp;
    }

    public void setiCountToUp(int iCountToUp) {
        this.iCountToUp = iCountToUp;
    }

    public int getiClickPrice() {
        return iClickPrice;
    }

    public void setiClickPrice(int iClickPrice) {
        this.iClickPrice = iClickPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemListOfLvl that = (ItemListOfLvl) o;

        if (iLvl != that.iLvl) return false;
        if (iCountToUp != that.iCountToUp) return false;
        return iClickPrice == that.iClickPrice;

    }

    @Override
    public int hashCode() {
        int result = iLvl;
        result = 31 * result + iCountToUp;
        result = 31 * result + iClickPrice;
        return result;
    }
}
