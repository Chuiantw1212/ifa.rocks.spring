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

    /**
     * Creates a new client profile.
     * @param req The request containing the client's name and email.
     * @return A DTO representing the newly created client's profile.
     */
    rocks.ifa.spring.domain.clientProfile.ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req);
}
