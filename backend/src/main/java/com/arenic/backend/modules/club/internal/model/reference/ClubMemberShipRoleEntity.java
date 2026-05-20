package com.arenic.backend.modules.club.internal.model.reference;

import com.arenic.backend.common.model.BaseReferenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "club_membership_roles")
public class ClubMemberShipRoleEntity extends BaseReferenceEntity {}
