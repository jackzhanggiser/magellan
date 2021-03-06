/**
  * Copyright 2015 Ram Sriharsha
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package magellan.catalyst

import magellan.TestSparkContext
import org.apache.spark.sql.magellan.dsl.expressions._
import org.scalatest.FunSuite

class GeohashIndexerSuite extends FunSuite with TestSparkContext {

  test("index points") {
    val sqlCtx = this.sqlContext
    val path = this.getClass.getClassLoader.getResource("testpoint/").getPath
    val df = sqlCtx.read.format("magellan").load(path)
    import sqlCtx.implicits._
    val index = df.withColumn("index", $"point" geohash 25).select('index).take(1)(0)(0)
    assert(index === Seq("9z109"))

    try {
      df.withColumn("index", $"point" geohash 23)
      assert(false)
    } catch {
      case e: Error => assert(true)
    }
  }
}
