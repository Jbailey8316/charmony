package svenhjol.charmony.core.client;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.core.particles.SimpleParticleType;

public record DeferredParticle(
    SimpleParticleType type,
    ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> registration
) { }
