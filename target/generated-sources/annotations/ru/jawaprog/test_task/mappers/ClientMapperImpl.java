package ru.jawaprog.test_task.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import ru.jawaprog.test_task.dao.entities.ClientDAO;
import ru.jawaprog.test_task.dao.entities.ClientDAO.ClientType;
import ru.jawaprog.test_task.web.entities.ClientDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-11T20:24:00+0300",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 11.0.12 (BellSoft)"
)
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDTO toDto(ClientDAO client) {
        if ( client == null ) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setId( client.getId() );
        clientDTO.setType( clientTypeToClientType( client.getType() ) );
        clientDTO.setFullName( client.getFullName() );

        return clientDTO;
    }

    @Override
    public List<ClientDTO> toDto(List<ClientDAO> clients) {
        if ( clients == null ) {
            return null;
        }

        List<ClientDTO> list = new ArrayList<ClientDTO>( clients.size() );
        for ( ClientDAO clientDAO : clients ) {
            list.add( toDto( clientDAO ) );
        }

        return list;
    }

    protected ru.jawaprog.test_task.web.entities.ClientDTO.ClientType clientTypeToClientType(ClientType clientType) {
        if ( clientType == null ) {
            return null;
        }

        ru.jawaprog.test_task.web.entities.ClientDTO.ClientType clientType1;

        switch ( clientType ) {
            case INDIVIDUAL: clientType1 = ru.jawaprog.test_task.web.entities.ClientDTO.ClientType.INDIVIDUAL;
            break;
            case ENTITY: clientType1 = ru.jawaprog.test_task.web.entities.ClientDTO.ClientType.ENTITY;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + clientType );
        }

        return clientType1;
    }
}
