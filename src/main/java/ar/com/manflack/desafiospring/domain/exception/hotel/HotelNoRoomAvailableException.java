package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelNoRoomAvailableException extends Exception
{
    public HotelNoRoomAvailableException()
    {
        super("We apologies, given the parameters, we don't have rooms available. Please consider change some "
                + "parameters.");
    }
}
