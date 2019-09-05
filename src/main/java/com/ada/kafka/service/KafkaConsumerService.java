
@Service
public class JobKafkaProducer {

    private final KafkaTemplate<String, JobKafkaMessage> jobKafkaTemplate;

    private final AvroDomainMapper avroDomainMapper;

    @Value("#{'${application.kafka.producer.jobEntityTopic}'}")
    private String jobTopic;

    public JobKafkaProducer(KafkaTemplate<String, JobKafkaMessage> jobKafkaTemplate,
            AvroDomainMapper avroDomainMapper) {
        this.jobKafkaTemplate = jobKafkaTemplate;
        this.avroDomainMapper = avroDomainMapper;
    }

    public void addToJobEntityQueue(JobDTO job, JobEvents jobEventType) {
        String key = job.getId().toString();
        JobKafkaMessage jobKafkaMessage = avroDomainMapper.map(job);
        jobKafkaMessage.setEventType(jobEventType);
        kafkaEventsLogger.info("Publishing message to topic {} with key {} and message {}", jobTopic, job.getId(),
                jobKafkaMessage);
        jobKafkaTemplate.send(jobTopic, key, jobKafkaMessage);
        kafkaEventsLogger.info("Response = {} ", getResponseStatusFromKafkaProducerAckFuture(kafkaProducerAckFuture));
    }

}
