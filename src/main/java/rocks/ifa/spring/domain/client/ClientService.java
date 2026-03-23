package rocks.ifa.spring.domain.client;

/**
 * An aggregate service responsible for orchestrating client-related data operations.
 */
public interface ClientService {

    /**
     * Assembles a complete data view for a client by aggregating data
     * from multiple sub-domain services.
     * @param uid The Firebase UID of the client.
     * @return A comprehensive DTO containing all of the client's financial profile data.
     */
    ClientFullDataRes getClientFullData(String uid);
}
