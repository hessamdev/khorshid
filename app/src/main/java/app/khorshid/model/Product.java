package app.khorshid.model;

public class Product
{
    public int ID;
    public String Name;
    public String Image;
    public int Price;
    public int Count;
    public int Category;

    public Product(int id, String name, String image, int count, int price, int category)
    {
        ID = id;
        Name = name;
        Image = image;
        Price = price;
        Count = count;
        Category = category;
    }
}
