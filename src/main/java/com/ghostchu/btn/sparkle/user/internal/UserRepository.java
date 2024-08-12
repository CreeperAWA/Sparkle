package com.ghostchu.btn.sparkle.user.internal;

import com.ghostchu.btn.sparkle.repository.SparkleCommonRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends SparkleCommonRepository<User, Long> {
    Optional<User> findByGithubIdentifier(@NonNull String githubIdentifier);

    List<User> findByNickname(String nickname);
}
