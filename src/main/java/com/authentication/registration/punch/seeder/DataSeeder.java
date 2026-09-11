package com.authentication.registration.punch.seeder;

import com.authentication.registration.punch.entities.Camp;
import com.authentication.registration.punch.entities.mmuUser;
import com.authentication.registration.punch.enums.UserStatus;
import com.authentication.registration.punch.repositories.CampRepository;
import com.authentication.registration.punch.repositories.mmuUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private final CampRepository campRepository;

    @Autowired
    private final mmuUserRepository mmuUserRepository;

    public DataSeeder(CampRepository campRepository, mmuUserRepository mmuUserRepository) {
        this.campRepository = campRepository;
        this.mmuUserRepository = mmuUserRepository;
    }

    @Override
    public void run(String... args){

        seedMmuUser();
        seedCamp();

    }

    private void seedMmuUser(){
        if(mmuUserRepository.count()>0) return ; // already seed one user now skip

        mmuUser mmuUser1 = new mmuUser();

        mmuUser1.setMmuId(1L);
        mmuUser1.setUserStatus(UserStatus.ACTIVE);

        mmuUser mmuUser2 = new mmuUser();
        mmuUser2.setMmuId(2L);
        mmuUser2.setUserStatus(UserStatus.ACTIVE);

        mmuUser mmuUser3 = new mmuUser();

        mmuUser3.setMmuId(3L);
        mmuUser3.setUserStatus(UserStatus.ACTIVE);

//        mmuUserRepository.save(mmuUser1);
//        mmuUserRepository.save(mmuUser2);
//        mmuUserRepository.save(mmuUser3);

        mmuUserRepository.saveAll(List.of(mmuUser1,mmuUser2,mmuUser3));

    }

    private void seedCamp(){
        if(campRepository.count()>0) return ; // already seed one user now skip

        Camp camp1 = new Camp();

        camp1.setMmuId(1L);
        camp1.setCampLatitude(23.2599);   // example: Bhopal coordinates
        camp1.setCampLongitude(77.4126);
        camp1.setCampLocation("Community Hall,Bhopal");
        camp1.setCampDate(LocalDateTime.now());
        camp1.setCampStartTime(LocalTime.of(8,0));
        camp1.setCampEndTime(LocalTime.of(18,0));

        Camp camp2 = new Camp();
        camp2.setMmuId(2L);
        camp2.setCampLatitude(21.2514);   // example: Raipur coordinates
        camp2.setCampLongitude(81.6296);
        camp2.setCampLocation("Govt School, Raipur");
        camp2.setCampDate(LocalDateTime.now());
        camp2.setCampStartTime(LocalTime.of(8,0));
        camp2.setCampEndTime(LocalTime.of(18,0));

        Camp camp3 = new Camp();

        camp3.setMmuId(3L);
        camp3.setCampLatitude(27.5686);
        camp3.setCampLongitude(70.7857);
        camp3.setCampLocation("Shankar Nagar, Nagpur");
        camp3.setCampDate(LocalDateTime.now());
        camp3.setCampStartTime(LocalTime.of(8,0));
        camp3.setCampEndTime(LocalTime.of(18,0));


//        campRepository.save(camp1);
//        campRepository.save(camp2);
//        campRepository.save(camp3);

        campRepository.saveAll(List.of(camp1,camp2,camp3));

    }

}
