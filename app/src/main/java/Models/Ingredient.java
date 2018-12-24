package Models;

public class Ingredient
{
    String key;

    public Ingredient(String key)
    {
        this.key = key;
    }

    public Ingredient()
    {
        this.key = "";
    }
    public String getKey()
    {
        return key;
    }

    public void setKey(String type)
    {
        this.key = type;
    }

}
