import SwiftUI

struct CatsView: View {
    @StateObject var store = CatsStore()
    
    @State var isFilterPresented = false
    
    var columns: [GridItem] =
            [.init(.adaptive(minimum: 96, maximum: 128))]
    
    var body: some View {
        NavigationView {
            VStack(spacing: 32) {
                ScrollView {
                    LazyVGrid(columns: columns, spacing: 2) {
                        ForEach(store.cats, id: \.id) { cat in
                            CatItem(cat: cat)
                        }
                        if (store.hasNextPage) {
                            loadingItem
                        }
                    }
                      .padding(.horizontal, 16)
                }
            }
            .onAppear {
                store.start()
            }
            .navigationTitle("Cat Viewer Demo")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(action: {
                        isFilterPresented = true
                    }, label: {
                        Image(systemName: "slider.horizontal.3")
                    })
                }
            }
            .sheet(isPresented: $isFilterPresented, onDismiss: handleFilterChange) {
                NavigationView {
                    CatsFilter(store: store)
                }
            }
        }
    }
    
    private var loadingItem: some View {
        VStack {
            ProgressView()
        }
        .frame(width: 96, height: 96)
        .onAppear(perform: {
            store.onScrolledToTheEnd()
        })
    }
    
    func handleFilterChange() {
        
    }
}

struct CatsView_Previews: PreviewProvider {
    static var previews: some View {
        CatsView()
    }
}
