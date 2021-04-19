package ar.com.manflack.desafiospring.app.dto;

public class CardDTOFixture
{
    public static final String type = "DEBIT";
    public static final String number = "1111222233334444";
    public static final Integer dues = 1;

    public static CardDTO withDefaults()
    {
        return new CardDTO(type, number, dues);
    }
}
