package ar.com.manflack.desafiospring.app.enums;

import ar.com.manflack.desafiospring.domain.exception.InvalidCardDuesException;

public enum CardInterestEnum
{
    CREDIT_DEBIT(1,0),
    CREDIT_3(3, 5.5),
    CREDIT_6(6, 10),
    CREDIT_9(9, 12),
    CREDIT_12(12, 20),
    CREDIT_18(18, 35);

    private final double interest;
    private final int dues;

    CardInterestEnum(int dues, double interest)
    {
        this.interest = interest;
        this.dues = dues;
    }

    public static double returnInterestByCreditDues(int dues) throws InvalidCardDuesException
    {
        for (CardInterestEnum card : CardInterestEnum.values())
        {
            if (card.dues == dues)
                return card.interest;
        }
        throw new InvalidCardDuesException();
    }
}
