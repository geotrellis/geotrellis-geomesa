# GeoTrellis GeoMesa

```scala
import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.geomesa._

val instance: GeoMesaInstance(
  tableName = ...,
  instanceName = ...,
  zookeepers = ...,
  users = ...,
  password = ...,
  useMock = ...
)

val reader = new GeoMesaFeatureReader(instance)
val writer = new GeoMesaFeatureWriter(instance)

val id: LayerId = ...
val query: Query = ... /* GeoMesa query type */

val spatialFeatureType: SimpleFeatureType = ... /* from geomesa - see their docs */

/* for some generic D, following GeoTrellis `Feature[G, D]` */
val res: RDD[SimpleFeature] = reader.read[Point, D](
  id,
  spatialFeatureType,
  query
)
```