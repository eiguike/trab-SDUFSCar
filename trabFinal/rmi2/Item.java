class Item{
  private String id;
  private byte[] data;

  public byte[] getData(){
    return data;
  }

  public String getId(){
    return id;
  }

  public Item(String id, byte [] data){
    this.id = id;
    this.data = data;
  }
}


