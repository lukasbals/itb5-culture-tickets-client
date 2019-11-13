package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface IEventDetailedViewDTO extends Remote {

    String getEventName() throws RemoteException;

    void setEventName(String eventName) throws RemoteException;

    String getArtists() throws RemoteException;

    void setArtists(String artists) throws RemoteException;

    String getDescription() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    String[] getGenres() throws RemoteException;

    void setGenres(String[] genre) throws RemoteException;

    String getLocation() throws RemoteException;

    void setLocation(String location) throws RemoteException;

    Float[] getPrices() throws RemoteException;

    void setPrices(Float[] prices) throws RemoteException;

    Float getMinPrice() throws RemoteException;

    Boolean isSeatReservationPossible() throws RemoteException;

    void setSeatReservationPossible(Boolean seatReservationPossible) throws RemoteException;

    Long[] getPlaceCategoriesId() throws RemoteException;

    void setPlaceCategoriesId(Long[] placeCategoriesId) throws RemoteException;

    String[] getPlaceCategories() throws RemoteException;

    void setPlaceCategories(String[] placeCategories) throws RemoteException;

    Integer[] getPlaceCategoriesAmount() throws RemoteException;

    void setPlaceCategoriesAmount(Integer[] amounts) throws RemoteException;

    String getCategory() throws RemoteException;

    void setCategory(String category) throws RemoteException;

    LocalDate getDate() throws RemoteException;

    void setDate(LocalDate date) throws RemoteException;

    Long getEventId() throws RemoteException;

    void setEventId(Long eventId) throws RemoteException;

}