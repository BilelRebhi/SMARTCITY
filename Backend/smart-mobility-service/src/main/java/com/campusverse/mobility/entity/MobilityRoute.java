package com.campusverse.mobility.entity;

/** Represents a mobility route from an origin to a destination, optionally using road segments. */
public class MobilityRoute {
    // id : Long
    // origin : String
    // destination : String
    // distanceKm : Double
    // relations: uses 0..* RoadSegment, destination 0..1 AccessiblePlace
}
