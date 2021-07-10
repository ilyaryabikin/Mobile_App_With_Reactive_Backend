package se.ifmo.mobile.server.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.domain.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByUsername(final String username);

  Flux<User> findAllByIdNotNull(final Pageable pageable);
}
