package com.food;

import com.enums.FoodType;
import com.utils.Position;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DAHA İLERİ SEVİYE TEST YAZIMI - Food Sınıfı
 *
 * Bu test dosyası gösterir:
 * 1. Enum ile çalışma (@EnumSource)
 * 2. Random fonksiyonları test etme
 * 3. Defensive copying test etme (güvenlik)
 * 4. Business logic testing (iş mantığı)
 * 5. State validation testing
 */
@DisplayName("Food Sınıfı Testleri 🐟")
class FoodTest {

    private Position testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new Position(5, 5);
    }

    // ========================================
    // CONSTRUCTOR TESTLER
    // ========================================

    @Test
    @DisplayName("Food nesnesi geçerli parametrelerle oluşturulabilmeli")
    void testConstructor_ValidParameters() {
        // ARRANGE
        FoodType type = FoodType.KRILL;
        int weight = 3;

        // ACT
        Food food = new Food(testPosition, type, weight);

        // ASSERT
        assertNotNull(food, "Food nesnesi null olmamalı");
        assertEquals(type, food.getType(), "FoodType doğru set edilmeli");
        assertEquals(weight, food.getWeight(), "Weight doğru set edilmeli");

        // Position defensive copy kontrolü
        Position returnedPos = food.getPosition();
        assertNotSame(testPosition, returnedPos,
                "Position defensive copy olmalı (farklı referans)");
        assertEquals(testPosition, returnedPos,
                "Ama değer olarak eşit olmalı");

        System.out.println("✓ Constructor testi başarılı: " + food);
    }

    @Test
    @DisplayName("Null position ile Food oluşturulursa exception fırlatmalı")
    void testConstructor_NullPosition() {
        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Food(null, FoodType.KRILL, 3),
                "Null position için exception fırlatılmalı"
        );

        // Exception mesajını da kontrol edebiliriz
        assertTrue(exception.getMessage().contains("position"),
                "Exception mesajında 'position' kelimesi olmalı");

        System.out.println("✓ Exception mesajı: " + exception.getMessage());
    }

    @Test
    @DisplayName("Null FoodType ile Food oluşturulursa exception fırlatmalı")
    void testConstructor_NullType() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new Food(testPosition, null, 3),
                "Null type için exception fırlatılmalı");
    }

    @ParameterizedTest
    @DisplayName("Geçersiz weight değerleri için exception")
    @org.junit.jupiter.params.provider.ValueSource(ints = {0, -1, -5, 6, 10, 100})
    void testConstructor_InvalidWeight(int invalidWeight) {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new Food(testPosition, FoodType.KRILL, invalidWeight),
                "Weight " + invalidWeight + " için exception fırlatılmalı");
    }

    @ParameterizedTest
    @DisplayName("Geçerli weight değerleri (1-5) ile Food oluşturulabilmeli")
    @org.junit.jupiter.params.provider.ValueSource(ints = {1, 2, 3, 4, 5})
    void testConstructor_ValidWeights(int validWeight) {
        // ACT
        Food food = new Food(testPosition, FoodType.SQUID, validWeight);

        // ASSERT
        assertNotNull(food);
        assertEquals(validWeight, food.getWeight());

        System.out.println("✓ Weight " + validWeight + " başarılı");
    }

    // ========================================
    // STATIC FACTORY METHOD TESTLER
    // ========================================

    @Test
    @DisplayName("createRandom metodu random bir Food oluşturmalı")
    void testCreateRandom() {
        // ACT - Birden fazla random food oluştur
        Food food1 = Food.createRandom(testPosition);
        Food food2 = Food.createRandom(testPosition);
        Food food3 = Food.createRandom(testPosition);

        // ASSERT - Basic validation
        assertNotNull(food1);
        assertNotNull(food2);
        assertNotNull(food3);

        // Weight 1-5 arasında olmalı
        assertTrue(food1.getWeight() >= 1 && food1.getWeight() <= 5,
                "Random weight 1-5 arasında olmalı");

        // FoodType null olmamalı
        assertNotNull(food1.getType(), "Random FoodType null olmamalı");

        System.out.println("✓ Random food 1: " + food1);
        System.out.println("✓ Random food 2: " + food2);
        System.out.println("✓ Random food 3: " + food3);
    }

    @Test
    @DisplayName("createRandom ile çoklu çağrıda çeşitlilik olmalı")
    void testCreateRandom_Variety() {
        // ACT - 100 random food oluştur
        boolean foundDifferentTypes = false;
        boolean foundDifferentWeights = false;

        FoodType firstType = null;
        int firstWeight = 0;

        for (int i = 0; i < 100; i++) {
            Food food = Food.createRandom(testPosition);

            if (i == 0) {
                firstType = food.getType();
                firstWeight = food.getWeight();
            } else {
                if (food.getType() != firstType) foundDifferentTypes = true;
                if (food.getWeight() != firstWeight) foundDifferentWeights = true;
            }

            if (foundDifferentTypes && foundDifferentWeights) break;
        }

        // ASSERT - 100 çağrıda farklı type ve weight görmüş olmalıyız
        assertTrue(foundDifferentTypes,
                "100 random food'da farklı type'lar görülmeli");
        assertTrue(foundDifferentWeights,
                "100 random food'da farklı weight'ler görülmeli");

        System.out.println("✓ Random çeşitlilik testi başarılı");
    }

    @Test
    @DisplayName("create static metodu belirtilen parametrelerle Food oluşturmalı")
    void testCreateMethod() {
        // ACT
        Food food = Food.create(testPosition, FoodType.MACKEREL, 4);

        // ASSERT
        assertNotNull(food);
        assertEquals(FoodType.MACKEREL, food.getType());
        assertEquals(4, food.getWeight());

        System.out.println("✓ Static create metodu başarılı");
    }

    // ========================================
    // GETTER METHOD TESTLER
    // ========================================

    @ParameterizedTest
    @DisplayName("Tüm FoodType'lar için getter metodları çalışmalı")
    @EnumSource(FoodType.class)
    void testGetters_AllFoodTypes(FoodType type) {
        // ACT
        Food food = new Food(testPosition, type, 3);

        // ASSERT
        assertEquals(type, food.getType());
        assertEquals(type.getShorthand(), food.getShorthand());
        assertEquals(type.toString(), food.getDisplayName());

        System.out.println("✓ " + type + " için getters başarılı");
    }

    @Test
    @DisplayName("getPosition defensive copy döndürmeli")
    void testGetPosition_DefensiveCopy() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.ANCHOVY, 2);

        // ACT
        Position pos1 = food.getPosition();
        Position pos2 = food.getPosition();

        // ASSERT
        // Her çağrıda yeni bir nesne dönmeli
        assertNotSame(pos1, pos2,
                "Her getPosition çağrısı yeni bir nesne dönmeli");

        // Ama değerler aynı olmalı
        assertEquals(pos1, pos2, "Değerler aynı olmalı");

        // Orijinal pozisyonla aynı değerde ama farklı nesne
        assertEquals(testPosition, pos1);
        assertNotSame(testPosition, pos1);

        System.out.println("✓ Defensive copy testi başarılı");
    }

    @Test
    @DisplayName("setPosition de defensive copy yapmalı")
    void testSetPosition_DefensiveCopy() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.KRILL, 1);
        Position newPos = new Position(7, 8);

        // ACT
        food.setPosition(newPos);
        Position retrieved = food.getPosition();

        // ASSERT
        // Değer olarak eşit ama referans olarak farklı
        assertEquals(newPos, retrieved, "Yeni pozisyon set edilmeli");
        assertNotSame(newPos, retrieved, "Defensive copy yapılmalı");

        // Orijinal position'ı değiştirmek Food'u etkilememeli
        newPos.setRow(999);
        assertNotEquals(999, retrieved.getRow(),
                "External pozisyon değişikliği Food'u etkilememeli");

        System.out.println("✓ SetPosition defensive copy testi başarılı");
    }

    // ========================================
    // HELPER METHOD TESTLER
    // ========================================

    @Test
    @DisplayName("isAtPosition metodu doğru pozisyonu kontrol etmeli")
    void testIsAtPosition() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.CRUSTACEAN, 3);
        Position samePos = new Position(5, 5);
        Position differentPos = new Position(3, 4);

        // ASSERT
        assertTrue(food.isAtPosition(samePos),
                "Aynı koordinatlarda true dönmeli");
        assertFalse(food.isAtPosition(differentPos),
                "Farklı koordinatlarda false dönmeli");

        System.out.println("✓ isAtPosition testi başarılı");
    }

    @Test
    @DisplayName("isType metodu doğru type kontrolü yapmalı")
    void testIsType() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.SQUID, 4);

        // ASSERT
        assertTrue(food.isType(FoodType.SQUID), "Kendi type'ı için true");
        assertFalse(food.isType(FoodType.KRILL), "Farklı type için false");
        assertFalse(food.isType(null), "Null için false");

        System.out.println("✓ isType testi başarılı");
    }

    @Test
    @DisplayName("isWeightInRange metodu doğru aralık kontrolü yapmalı")
    void testIsWeightInRange() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.MACKEREL, 3);

        // ASSERT
        assertTrue(food.isWeightInRange(1, 5), "1-5 aralığında");
        assertTrue(food.isWeightInRange(3, 3), "Tam 3'te");
        assertTrue(food.isWeightInRange(2, 4), "2-4 aralığında");
        assertFalse(food.isWeightInRange(4, 5), "4-5 aralığında değil");
        assertFalse(food.isWeightInRange(1, 2), "1-2 aralığında değil");

        System.out.println("✓ Weight range testi başarılı");
    }

    @Test
    @DisplayName("isLightweight metodu hafif yiyecekleri tespit etmeli")
    void testIsLightweight() {
        // ARRANGE & ACT & ASSERT
        assertTrue(new Food(testPosition, FoodType.KRILL, 1).isLightweight());
        assertTrue(new Food(testPosition, FoodType.KRILL, 2).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 3).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 4).isLightweight());
        assertFalse(new Food(testPosition, FoodType.KRILL, 5).isLightweight());

        System.out.println("✓ Lightweight testi başarılı");
    }

    @Test
    @DisplayName("isHeavyweight metodu ağır yiyecekleri tespit etmeli")
    void testIsHeavyweight() {
        // ARRANGE & ACT & ASSERT
        assertFalse(new Food(testPosition, FoodType.SQUID, 1).isHeavyweight());
        assertFalse(new Food(testPosition, FoodType.SQUID, 2).isHeavyweight());
        assertFalse(new Food(testPosition, FoodType.SQUID, 3).isHeavyweight());
        assertTrue(new Food(testPosition, FoodType.SQUID, 4).isHeavyweight());
        assertTrue(new Food(testPosition, FoodType.SQUID, 5).isHeavyweight());

        System.out.println("✓ Heavyweight testi başarılı");
    }

    // ========================================
    // STATE VALIDATION TESTLER
    // ========================================

    @Test
    @DisplayName("validateState metodu geçerli Food için true dönmeli")
    void testValidateState_ValidFood() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.ANCHOVY, 3);

        // ACT & ASSERT
        assertTrue(food.validateState(), "Geçerli Food state valid olmalı");
    }

    @Test
    @DisplayName("getStateSummary ve getDetailedDescription metodları çalışmalı")
    void testDescriptionMethods() {
        // ARRANGE
        Food food = new Food(testPosition, FoodType.MACKEREL, 5);

        // ACT
        String summary = food.getStateSummary();
        String detailed = food.getDetailedDescription();
        String toString = food.toString();

        // ASSERT
        assertNotNull(summary, "Summary null olmamalı");
        assertNotNull(detailed, "Detailed null olmamalı");
        assertNotNull(toString, "toString null olmamalı");

        assertTrue(summary.contains("Mackerel"), "Summary type içermeli");
        assertTrue(summary.contains("5"), "Summary weight içermeli");
        assertTrue(detailed.contains("position"), "Detailed position içermeli");

        System.out.println("📋 Summary: " + summary);
        System.out.println("📋 Detailed: " + detailed);
        System.out.println("📋 toString: " + toString);
    }

    // ========================================
    // EQUALS VE HASHCODE TESTLER
    // ========================================

    @Test
    @DisplayName("equals metodu aynı özelliklere sahip Food'lar için true dönmeli")
    void testEquals_SameProperties() {
        // ARRANGE
        Food food1 = new Food(testPosition, FoodType.KRILL, 2);
        Food food2 = new Food(new Position(5, 5), FoodType.KRILL, 2);

        // ASSERT
        assertEquals(food1, food2, "Aynı özellikler eşit olmalı");
        assertEquals(food1.hashCode(), food2.hashCode(),
                "Eşit objeler aynı hashCode'a sahip olmalı");
    }

    @Test
    @DisplayName("equals metodu farklı özelliklere sahip Food'lar için false dönmeli")
    void testEquals_DifferentProperties() {
        // ARRANGE
        Food food1 = new Food(testPosition, FoodType.KRILL, 2);
        Food food2 = new Food(testPosition, FoodType.SQUID, 2);  // Farklı type
        Food food3 = new Food(testPosition, FoodType.KRILL, 3);  // Farklı weight
        Food food4 = new Food(new Position(7, 7), FoodType.KRILL, 2); // Farklı pos

        // ASSERT
        assertNotEquals(food1, food2, "Farklı type eşit olmamalı");
        assertNotEquals(food1, food3, "Farklı weight eşit olmamalı");
        assertNotEquals(food1, food4, "Farklı position eşit olmamalı");
        assertNotEquals(food1, null, "Null ile eşit olmamalı");
    }

    // ========================================
    // COPY VE COMPARE TESTLER
    // ========================================

    @Test
    @DisplayName("copyAtPosition metodu yeni pozisyonda kopya oluşturmalı")
    void testCopyAtPosition() {
        // ARRANGE
        Food original = new Food(testPosition, FoodType.ANCHOVY, 4);
        Position newPos = new Position(8, 9);

        // ACT
        Food copy = original.copyAtPosition(newPos);

        // ASSERT
        assertNotSame(original, copy, "Farklı nesneler olmalı");
        assertEquals(original.getType(), copy.getType(), "Type aynı olmalı");
        assertEquals(original.getWeight(), copy.getWeight(), "Weight aynı olmalı");
        assertEquals(newPos, copy.getPosition(), "Yeni position set edilmeli");
        assertNotEquals(original.getPosition(), copy.getPosition(),
                "Position farklı olmalı");
    }

    @Test
    @DisplayName("compareByWeight metodu weight'e göre karşılaştırmalı")
    void testCompareByWeight() {
        // ARRANGE
        Food light = new Food(testPosition, FoodType.KRILL, 1);
        Food medium = new Food(testPosition, FoodType.SQUID, 3);
        Food heavy = new Food(testPosition, FoodType.MACKEREL, 5);

        // ASSERT
        assertTrue(light.compareByWeight(medium) < 0,
                "Hafif < Orta");
        assertTrue(medium.compareByWeight(heavy) < 0,
                "Orta < Ağır");
        assertTrue(heavy.compareByWeight(light) > 0,
                "Ağır > Hafif");
        assertEquals(0, medium.compareByWeight(
                        new Food(new Position(1, 1), FoodType.KRILL, 3)),
                "Aynı weight = 0");
    }

    // ========================================
    // INTEGRATION TEST
    // ========================================

    @Test
    @DisplayName("Entegrasyon: Food yaşam döngüsü")
    void testIntegration_FoodLifecycle() {
        System.out.println("\n=== Food Yaşam Döngüsü ===");

        // 1. Random food oluşturma
        Food food = Food.createRandom(new Position(3, 4));
        System.out.println("1️⃣ Oluşturuldu: " + food.getDetailedDescription());
        assertTrue(food.validateState(), "State geçerli olmalı");

        // 2. Pozisyon değiştirme
        Position newPos = new Position(7, 8);
        food.setPosition(newPos);
        System.out.println("2️⃣ Taşındı: " + food.getPosition());
        assertTrue(food.isAtPosition(newPos), "Yeni pozisyonda olmalı");

        // 3. Özellik kontrolleri
        boolean isLight = food.isLightweight();
        boolean isHeavy = food.isHeavyweight();
        System.out.println("3️⃣ Lightweight: " + isLight + ", Heavyweight: " + isHeavy);
        assertNotEquals(isLight, isHeavy, "Hem light hem heavy olamaz");

        // 4. Kopya oluşturma
        Food copy = food.copyAtPosition(new Position(1, 1));
        System.out.println("4️⃣ Kopya oluşturuldu: " + copy);
        assertNotSame(food, copy, "Farklı nesneler");
        assertEquals(food.getType(), copy.getType(), "Aynı özellikler");

        System.out.println("✓ Food lifecycle testi başarılı");
    }

    // ========================================
    // EDGE CASES
    // ========================================

    @Test
    @DisplayName("Edge case: Minimum ve maximum weight değerleri")
    void testEdgeCase_MinMaxWeight() {
        // ARRANGE & ACT
        Food minFood = new Food(testPosition, FoodType.KRILL, 1);
        Food maxFood = new Food(testPosition, FoodType.MACKEREL, 5);

        // ASSERT
        assertEquals(1, minFood.getWeight(), "Minimum weight 1");
        assertEquals(5, maxFood.getWeight(), "Maximum weight 5");
        assertTrue(minFood.isLightweight(), "Weight 1 lightweight");
        assertTrue(maxFood.isHeavyweight(), "Weight 5 heavyweight");
    }

    @Test
    @DisplayName("Edge case: Tüm FoodType kombinasyonları oluşturulabilmeli")
    void testEdgeCase_AllFoodTypeCombinations() {
        // ACT & ASSERT
        for (FoodType type : FoodType.values()) {
            for (int weight = 1; weight <= 5; weight++) {
                Food food = new Food(testPosition, type, weight);
                assertNotNull(food,
                        "Food(" + type + ", " + weight + ") oluşturulabilmeli");
                assertEquals(type, food.getType());
                assertEquals(weight, food.getWeight());
            }
        }

        System.out.println("✓ " + (FoodType.values().length * 5) +
                " farklı kombinasyon test edildi");
    }
}