package ar.com.manflack.desafiospring.app.enums;

public enum RoomTypesEnum
{
    SINGLE("single", 1),
    DOBLE("doble", 2),
    TRIPLE("triple", 3),
    MULTIPLE("múltiple", 4);

    private final String type;
    private final int amountPeople;

    RoomTypesEnum(String type, int amountPeople)
    {
        this.type = type;
        this.amountPeople = amountPeople;
    }

    public static RoomTypesEnum returnRoomTypeGivenAmountPeople(int amountPeople)
    {
        for (RoomTypesEnum roomTypesEnum : RoomTypesEnum.values())
        {
            if (roomTypesEnum.amountPeople == amountPeople)
                return roomTypesEnum;
        }
        return null;
    }

    public String getType()
    {
        return type;
    }
}
