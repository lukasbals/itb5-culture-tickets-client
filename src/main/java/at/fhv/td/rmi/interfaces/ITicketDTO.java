package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;

public interface ITicketDTO extends Remote{

	int getTicketNumber();

	void setTicketNumber(int ticketNumber);

	String getCategoryname();

	void setCategoryname(String categoryname);

	String getClient();

	void setClient(String client);

	Float getPrice();

	void setPrice(Float price);

	Long getId();

	void setId(Long id);

}