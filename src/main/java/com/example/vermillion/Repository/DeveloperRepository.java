package com.example.vermillion.Repository;



import com.example.vermillion.Model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository <Developer, Long>{

}
