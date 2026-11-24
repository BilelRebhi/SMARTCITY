import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MunicipalService } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-catalogue',
  imports: [RouterLink, FormsModule],
  templateUrl: './catalogue.html',
  styleUrl: './catalogue.css'
})
export class CatalogueComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);

  services: MunicipalService[] = [];
  loading = true;
  error: string | null = null;
  searchTerm = '';
  selectedCategory = '';

  ngOnInit(): void {
    this.api.getServices().subscribe({
      next: (data) => {
        this.services = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  get categories(): string[] {
    const cats = new Set<string>();
    for (const s of this.services) {
      if (s.category) cats.add(s.category);
    }
    return Array.from(cats);
  }

  get filteredServices(): MunicipalService[] {
    const term = this.searchTerm.trim().toLowerCase();
    return this.services.filter((s) => {
      const matchesCategory = !this.selectedCategory || s.category === this.selectedCategory;
      const matchesTerm =
        !term ||
        s.name.toLowerCase().includes(term) ||
        s.description.toLowerCase().includes(term) ||
        (s.requirements && s.requirements.toLowerCase().includes(term));
      return matchesCategory && matchesTerm;
    });
  }

  parseRequirements(req: string): string[] {
    return req
      .split(';')
      .map((r) => r.trim())
      .filter((r) => r.length > 0);
  }
}
