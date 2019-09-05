import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AvroDomainMapper {

    JobDTO map(JobKafkaMessage jobKafkaMessage);

}