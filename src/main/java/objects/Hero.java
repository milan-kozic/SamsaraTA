package objects;

import com.github.javafaker.Faker;
import com.google.gson.annotations.SerializedName;
import data.HeroClass;
import org.testng.Assert;
import utils.DateTimeUtils;

import java.util.Date;
import java.util.Objects;

public class Hero {

    private static final String[] heroClasses = {HeroClass.WARRIOR, HeroClass.GUARDIAN, HeroClass.REVENANT, HeroClass.ENGINEER, HeroClass.THIEF, HeroClass.RANGER, HeroClass.ELEMENTALIST, HeroClass.NECROMANCER, HeroClass.MESMER};

    // @Expose
    @SerializedName("name")
    private String heroName;

    @SerializedName("type")
    private String heroClass;

    @SerializedName("level")
    private Integer heroLevel;

    private String username;

    private Long createdAt;

    public Hero(String heroName, String heroClass, int heroLevel, String username, Date createdAt) {
        this.setHeroName(heroName);
        this.setHeroClass(heroClass);
        this.setHeroLevel(heroLevel);
        this.setUsername(username);
        this.setCreatedAt(createdAt);
    }

    private Hero(String heroName, String heroClass, int heroLevel, String username) {
        this.setHeroName(heroName);
        this.setHeroClass(heroClass);
        this.setHeroLevel(heroLevel);
        this.setUsername(username);
        this.setCreatedAt(null);
    }

    private Hero(String heroName, String heroClass, int heroLevel) {
        this.setHeroName(heroName);
        this.setHeroClass(heroClass);
        this.setHeroLevel(heroLevel);
        this.setUsername(null);
        this.setCreatedAt(null);
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(String heroClass) {
        this.heroClass = heroClass;
    }

    public Integer getHeroLevel() {
        return heroLevel;
    }

    public void setHeroLevel(Integer heroLevel) {
        this.heroLevel = heroLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        if(createdAt == null) {
            return null;
        }
        return DateTimeUtils.getDateTime(this.createdAt);
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Date date) {
        if (date == null) {
            this.createdAt = null;
        } else {
            this.createdAt = date.getTime();
        }
    }

    public static Hero createNewUniqueHero(User user, String sHeroName, String sHeroClass, int iHeroLevel) {
        String heroName = sHeroName + DateTimeUtils.getDateTimeStamp();
        if (heroName.length() > 25) {
            Assert.fail("Hero Name '" + heroName + "' cannot be longer than 25 characters!");
        }
        return new Hero(heroName, sHeroClass, iHeroLevel, user.getUsername());
    }

    public static Hero createNewUniqueHero(User user, String sHeroName) {
        String heroName = sHeroName + DateTimeUtils.getDateTimeStamp();
        if (heroName.length() > 25) {
            Assert.fail("Hero Name '" + heroName + "' cannot be longer than 25 characters!");
        }
        String username = user.getUsername();
        int heroLevel = createRandomHeroLevel();
        String heroClass = createRandomHeroClass();
        return new Hero(heroName, heroClass, heroLevel, username);
    }

    private static int createRandomHeroLevel() {
        Faker faker = new Faker();
        return faker.random().nextInt(0, 80);
    }

    private static String createRandomHeroClass() {
        Faker faker = new Faker();
        return heroClasses[faker.random().nextInt(0, 8)];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hero)) return false;
        Hero hero = (Hero) o;
        return Objects.equals(getHeroName(), hero.getHeroName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeroName());
    }

    @Override
    public String toString() {
        return "Hero {"
                + "Hero Name: " + getHeroName() + ", "
                + "Hero Class: " + getHeroClass() + ", "
                + "Hero Level: " + getHeroLevel() + ", "
                + "Username: " + getUsername() + ", "
                + "Created at: " + getCreatedAt() + "}";
    }

}
