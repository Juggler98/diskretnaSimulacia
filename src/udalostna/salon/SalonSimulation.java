package udalostna.salon;

import generators.*;
import simCores.EventCore;
import udalostna.stanok.ZakaznikStanku;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SalonSimulation extends EventCore {

    private final LinkedList<ZakaznikSalonu> radRecepcia = new LinkedList<>();
    private final LinkedList<ZakaznikSalonu> radUces = new LinkedList<>();
    private final LinkedList<ZakaznikSalonu> radLicenie = new LinkedList<>();
    private final LinkedList<ZakaznikSalonu> radPlatba = new LinkedList<>();

    private final Random seedGenerator = new Random();
    private final RandUniformContinuous randObjednavka = new RandUniformContinuous(200 - 120, 200 + 120, seedGenerator);
    private final RandTriangular randHlbkoveCistenie = new RandTriangular(360, 900, 540, seedGenerator);
    private final RandUniformContinuous randPlatba = new RandUniformContinuous(180 - 50, 180 + 50, seedGenerator);
    private final RandUniformDiscrete randUcetJednoduchy = new RandUniformDiscrete(10, 30, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesZlozity = {new EmpiricDiscrete(30, 60, 0.4), new EmpiricDiscrete(61, 120, 0.6)};
    private final RandEmpiricDiscrete randUcesZlozity = new RandEmpiricDiscrete(empiricDiscretesUcesZlozity, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesSvadobny = {new EmpiricDiscrete(50, 60, 0.2), new EmpiricDiscrete(61, 100, 0.3), new EmpiricDiscrete(101, 150, 0.5)};
    private final RandEmpiricDiscrete randUcesSvadobny = new RandEmpiricDiscrete(empiricDiscretesUcesSvadobny, seedGenerator);
    private final RandUniformDiscrete randLicenieJednoduche = new RandUniformDiscrete(10, 25, seedGenerator);
    private final RandUniformDiscrete randLicenieZlozite = new RandUniformDiscrete(20, 100, seedGenerator);




    @Override
    public void beforeReplications() {

    }

    @Override
    public void beforeReplication() {

    }

    @Override
    public void replication() {

    }

    @Override
    public void afterReplication() {

    }

    @Override
    public void afterReplications() {

    }
}
