package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 2/27/21.
 */
public class PersonRepositoryImpl implements PersonRepository {

    Person micheal = new Person(1, "Micheal", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");


    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> person.getId() == id).next();

    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(micheal, fiona, sam, jesse);
    }
}
