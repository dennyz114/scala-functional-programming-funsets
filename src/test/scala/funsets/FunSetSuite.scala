package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }


  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val u12 = union(s1, s2)
    val u23 = union(s2, s3)
    val u13 = union(s1, s3)
    val u123 = union(union(s1, s2), s3)

  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * .ignore annotation.
   */
  test("singleton set one contains one") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets:
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("Intersect contains repeated elements of both sets") {
    new TestSets :
      val i2 = intersect(u12, u23)
      assert(contains(i2, 2))
      assert(!contains(i2, 3))
      assert(!contains(i2, 1))
  }

   test("Diff contains elements in first except for all in second") {
     new TestSets :
       val d1 = diff(u12, u23)
       val d13 = diff(u13, s2)
       assert(contains(d1, 1))
       assert(!contains(d1, 2))
       assert(contains(d13, 1))
       assert(contains(d13, 3))
   }

   test("Filter contains elements in set for which function holds") {
     new TestSets :
       val evenElements = filter(u12, x => x%2 == 0)
       assert(contains(evenElements, 2))
       assert(!contains(evenElements, 1))
   }

   test("Forall returns whether all elements in set holds the function") {
     new TestSets :
       assert(forall(u123, x => x > 0))
       assert(!forall(u123, x => x < 0))
   }
   
   test("Exists returns whether at least one element holds the function") {
     new TestSets :
       assert(exists(u123, x => x%3 == 0))
       assert(!exists(u123, x => x%7 == 0))
   }
   
   test("Map contains elements with the function applied") {
     new TestSets :
       val u149 = map(u123, x => x*x)
       assert(contains(u149, 9))
       assert(contains(u149, 4))
       assert(!contains(u149, 2))
   }



  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
