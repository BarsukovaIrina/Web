package ObjectClass;

import javax.xml.bind.*;
import java.io.File;

public class JaxbWriter {

    JaxbWriter() throws JAXBException {
        User user4 = new User();
        user4.setID(1);
        user4.setName("Ivan");
        user4.setLogin("cartel");
        user4.setPassword("11s1");

        JAXBContext context = JAXBContext.newInstance(User.class);
        Marshaller marsh = context.createMarshaller();
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marsh.marshal(user4, new File("result.xml"));
    }
}
