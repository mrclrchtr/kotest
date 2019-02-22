package io.kotlintest.runner.console

import io.kotlintest.Description
import io.kotlintest.TestFilterResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.specs.FeatureSpec
import io.kotlintest.specs.FreeSpec
import io.kotlintest.specs.FunSpec
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.specs.StringSpec
import io.kotlintest.specs.WordSpec

class SpecAwareTestFilterTest : FunSpec() {

  init {

    test("should filter for fun specs") {
      val root = Description.spec(FunSpecs::class)
      SpecAwareTestFilter("test a", FunSpecs::class).filter(root.append("test")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("test", FunSpecs::class).filter(root.append("test a")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("test a", FunSpecs::class).filter(root.append("test a")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("test a", FunSpecs::class).filter(root.append("test a b")) shouldBe TestFilterResult.Exclude
    }

    test("should filter for strings specs") {
      val r = Description.spec(StringSpecs::class)

      SpecAwareTestFilter("test a", StringSpecs::class).filter(r.append("test")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("test", StringSpecs::class).filter(r.append("test a")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("test a", StringSpecs::class).filter(r.append("test a")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("test a", StringSpecs::class).filter(r.append("test a b")) shouldBe TestFilterResult.Exclude
    }

    test("should filter for should specs") {
      val r = Description.spec(ShouldSpecs::class)

      SpecAwareTestFilter("should test a", ShouldSpecs::class)
          .filter(r.append("should test")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("should test", ShouldSpecs::class)
          .filter(r.append("should test a")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("should test a", ShouldSpecs::class)
          .filter(r.append("should test a")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("should test a", ShouldSpecs::class)
          .filter(r.append("should test a b")) shouldBe TestFilterResult.Exclude
    }

    test("should filter for word specs") {
      val r = Description.spec(WordSpecs::class)

      SpecAwareTestFilter("a should b", WordSpecs::class)
          .filter(r.append("a should")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a should b", WordSpecs::class)
          .filter(r.append("a should").append("b")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a should b", WordSpecs::class)
          .filter(r.append("a should").append("c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a should b", WordSpecs::class)
          .filter(r.append("b should")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a should b", WordSpecs::class)
          .filter(r.append("b should").append("b")) shouldBe TestFilterResult.Exclude
    }

    test("should filter for free specs") {
      val r = Description.spec(FreeSpecs::class)

      SpecAwareTestFilter("a", FreeSpecs::class)
          .filter(r.append("a")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a", FreeSpecs::class)
          .filter(r.append("b").append("b")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b", FreeSpecs::class)
          .filter(r.append("a")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a -- b", FreeSpecs::class)
          .filter(r.append("a").append("b")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a -- b", FreeSpecs::class)
          .filter(r.append("c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b", FreeSpecs::class)
          .filter(r.append("a").append("c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b", FreeSpecs::class)
          .filter(r.append("b").append("b")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("a")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("a").append("b")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("a").append("b").append("c")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("b").append("b").append("c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("b").append("c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("a -- b -- c", FreeSpecs::class)
          .filter(r.append("a").append("b").append("c").append("d")) shouldBe TestFilterResult.Include
    }

    test("should filter for behavior specs") {
      val r = Description.spec(BehaviorSpecs::class)

      SpecAwareTestFilter("Given: a", BehaviorSpecs::class)
          .filter(r.append("Given: a")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Given: a", BehaviorSpecs::class)
          .filter(r.append("Given: aa")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Given: a", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Given: a", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b").append("Then: c")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("Given: a When: b", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Given: a When: b", BehaviorSpecs::class)
          .filter(r.append("Given: aa")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Given: a When: b", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: bb")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Given: a When: b", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b").append("Then: c")) shouldBe TestFilterResult.Include

      SpecAwareTestFilter("Given: a When: b Then: c", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b").append("Then: c")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Given: a When: b Then: c", BehaviorSpecs::class)
          .filter(r.append("Given: aa").append("When: b").append("Then: c")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Given: a When: b Then: c", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: bb").append("Then: c")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Given: a When: b Then: c", BehaviorSpecs::class)
          .filter(r.append("Given: a").append("When: b").append("Then: cc")) shouldBe TestFilterResult.Exclude
    }

    test("should filter for feature specs") {
      val r = Description.spec(FeatureSpecs::class)

      SpecAwareTestFilter("Feature: a", FeatureSpecs::class)
          .filter(r.append("Feature: a")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Feature: a", FeatureSpecs::class)
          .filter(r.append("Feature: aa")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Feature: a", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: b")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Feature: a", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: c")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Feature: b", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: b")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Feature: b", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: c")) shouldBe TestFilterResult.Exclude

      SpecAwareTestFilter("Feature: a Scenario: b", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: b")) shouldBe TestFilterResult.Include
      SpecAwareTestFilter("Feature: a Scenario: b", FeatureSpecs::class)
          .filter(r.append("Feature: aa")) shouldBe TestFilterResult.Exclude
      SpecAwareTestFilter("Feature: a Scenario: b", FeatureSpecs::class)
          .filter(r.append("Feature: a").append("Scenario: bb")) shouldBe TestFilterResult.Exclude
    }
  }
}

class BehaviorSpecs : BehaviorSpec()
class FeatureSpecs : FeatureSpec()
class FreeSpecs : FreeSpec()
class FunSpecs : FunSpec()
class ShouldSpecs : ShouldSpec()
class StringSpecs : StringSpec()
class WordSpecs : WordSpec()
